package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.YoutubeSession;
import br.com.elissonsilva.ptzwebcontrol.backend.repository.YoutubeSessionRepository;

import br.com.elissonsilva.ptzwebcontrol.backend.services.YoutubeService;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/youtube")
public class YoutubeController {

    private static final String REDIRECT_URI = "http://localhost/api/youtube/callback/";

    @Autowired
    private YoutubeSessionRepository youtubeRepository;

    @Autowired
    private YoutubeService youtubeService;

    private static Logger log = LoggerFactory.getLogger(YoutubeController.class);

    @GetMapping("/isConnected")
    public ResponseEntity<Boolean> isConnected() throws UnknownHostException {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/connect")
    public ResponseEntity<Void> getConnect() throws IOException {

        YoutubeSession youtubeSession = youtubeRepository.findFirstByOrderByCodeAsc();
        if(youtubeSession != null && youtubeSession.getAccessToken() != null)
        {
            log.info("Já está conectado");
            youtubeService.setCredential(youtubeSession);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else if(youtubeSession != null && youtubeSession.getCode() != null)
        {
            log.info("Tem apenas o code (falta access_token)");
            String url = REDIRECT_URI + "?code=" + youtubeSession.getCode();
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
        }
        else
        {
            log.info("Ainda não está conectado ... redirecionando para login");
            String url = youtubeService.getClientRequestUrl(REDIRECT_URI);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> getCallback(YoutubeSession youtubeSession) throws IOException {
        String code = youtubeSession.getCode();
        if(code != null) {
            log.info("code: " + code);
            //
            try {
                GoogleTokenResponse token = youtubeService.getAuthorizationCodeToken(code, REDIRECT_URI);
                //
                youtubeSession.setTokenResponse(token);
                //
            } catch (TokenResponseException e) {
                log.error(e.getMessage(), e);
                if(e.getStatusCode() == 400)
                {
                    youtubeRepository.delete(youtubeSession);
                    String url = "http://localhost/api/youtube/connect";
                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
                }
                return ResponseEntity.status(e.getStatusCode()).build();
            } catch (GeneralSecurityException e) {
                log.error(e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            //
            youtubeRepository.save(youtubeSession);
            youtubeService.setCredential(youtubeSession);
            log.info("sucesso");
            //
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/channelsList")
    public ResponseEntity<List<Channel>> getChannelsList() throws IOException, GeneralSecurityException {
        YoutubeSession youtubeSession = youtubeRepository.findFirstByOrderByCodeAsc();
        if(youtubeSession == null || youtubeSession.getAccessToken() == null)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try
        {
            youtubeService.setCredential(youtubeSession);
            ChannelListResponse response = youtubeService.getChannelList();
            if (response == null || response.getItems() == null || response.getItems().size() == 0)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(response.getItems(), HttpStatus.OK);
        }
        catch(GoogleJsonResponseException e)
        {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(e.getStatusCode()).build();
        }
        catch (Exception e)
        {
            throw e;
        }
    }

}
