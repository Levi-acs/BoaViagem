package com.example.boaviagem.calendar;


import com.example.boaviagem.domain.*;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport; // Substitui o AndroidHttp
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
public class CalendarService {

    private Calendar calendar;
    private String nomeConta;

    // Mantivemos o construtor igual ao do livro (recebendo String)
    // para não quebrar sua Activity
    public CalendarService(String nomeConta, String tokenAcesso) {
        this.nomeConta = nomeConta;




        // 1. Configura a credencial com o token recebido
        GoogleCredential credencial = new GoogleCredential();
        credencial.setAccessToken(tokenAcesso);

        // 2. CORREÇÃO: Usamos NetHttpTransport em vez de AndroidHttp
        HttpTransport transport = new NetHttpTransport();

        // 3. CORREÇÃO: Usamos getDefaultInstance para performance e compatibilidade
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        // 4. Construção do objeto Calendar (sem precisar de setJsonHttpRequestInitializer)
        calendar = new Calendar.Builder(transport, jsonFactory, credencial)
                .setApplicationName("BoaViagem") // Ou use Constantes.APP_NAME
                .build();
    }

    public String criarEvento(Viagem viagem) {
        Event evento = new Event();
        evento.setSummary(viagem.getDestino());

        List<EventAttendee> participantes =
                Arrays.asList(new EventAttendee().setEmail(nomeConta));
        evento.setAttendees(participantes);

        // Configuração das datas
        DateTime inicio = new DateTime(viagem.getDataChegada(), TimeZone.getDefault());
        DateTime fim = new DateTime(viagem.getDataSaida(), TimeZone.getDefault());

        evento.setStart(new EventDateTime().setDateTime(inicio));
        evento.setEnd(new EventDateTime().setDateTime(fim));

        try {
            // Executa a inserção na nuvem
            Event eventoCriado = calendar.events().insert("primary", evento).execute();
            // Nota: "primary" é um alias seguro para o calendário principal do usuário logado,
            // mas você pode usar a variável 'nomeConta' se preferir, como estava antes.

            return eventoCriado.getId();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar evento no Google Calendar", e);
        }


    }
    public void removerEvento(String idEvento){
        try {
            calendar.events()
                    .delete(nomeConta,idEvento)
                    .execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void atualizrEvento(String idEvento, Event eventoAtualizado){
        try {
            calendar.events()
                    .update(nomeConta,idEvento,eventoAtualizado)
                    .execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

