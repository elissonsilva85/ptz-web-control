package br.com.elissonsilva.ptzwebcontrol.backend.controllers;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.YoutubeLiveBroadcast;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.YoutubeSession;
import br.com.elissonsilva.ptzwebcontrol.backend.repository.YoutubeSessionRepository;

import br.com.elissonsilva.ptzwebcontrol.backend.services.YoutubeService;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.LiveBroadcastListResponse;
import com.google.api.services.youtube.model.LiveStreamListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/api/youtube")
public class YoutubeController {

    @Autowired
    private YoutubeSessionRepository youtubeRepository;

    @Autowired
    private YoutubeService youtubeService;

    private static Logger log = LoggerFactory.getLogger(YoutubeController.class);

    private static YoutubeSession youtubeSession;

    private boolean isConnected() {
        youtubeSession = youtubeRepository.findFirstByOrderByCodeAsc();
        return !(youtubeSession == null || youtubeSession.getAccessToken() == null);
    }

    @GetMapping("/isConnected")
    public ResponseEntity<Boolean> getIsConnected() throws UnknownHostException {
        return new ResponseEntity<>(isConnected(), HttpStatus.OK);
    }

    @GetMapping("/disconnect")
    public ResponseEntity<Void> getDisconnect() throws IOException {
        if(!isConnected())
            return new ResponseEntity<>(null, HttpStatus.OK);

        youtubeRepository.delete(youtubeSession);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/connect")
    public ResponseEntity<String> getConnect() throws IOException {
        isConnected();

        if(youtubeSession != null && youtubeSession.getAccessToken() != null)
        {
            log.info("Ja esta conectado");
            youtubeService.setCredential(youtubeSession);
            return new ResponseEntity<>("<script>window.close()</script>", HttpStatus.OK);
        }
        else if(youtubeSession != null && youtubeSession.getCode() != null)
        {
            log.info("Tem apenas o code (falta access_token)");
            String url = youtubeService.getRedirectUri("callback") + "?code=" + youtubeSession.getCode();
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
        }
        else
        {
            log.info("Ainda nao esta conectado ... redirecionando para login");
            String url = youtubeService.getClientRequestUrl(youtubeService.getRedirectUri("callback"));
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<String> getCallback(YoutubeSession ytSession) throws IOException {
        String code = ytSession.getCode();
        if(code != null) {
            log.info("code: " + code);
            //
            try {
                GoogleTokenResponse token = youtubeService.getAuthorizationCodeToken(code, youtubeService.getRedirectUri("callback"));
                //
                ytSession.setTokenResponse(token);
                //
            } catch (TokenResponseException e) {
                log.error(e.getMessage(), e);
                if(e.getStatusCode() == 400)
                {
                    youtubeRepository.delete(ytSession);
                    String url = youtubeService.getRedirectUri("connect");
                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
                }
                return ResponseEntity.status(e.getStatusCode()).build();
            } catch (GeneralSecurityException e) {
                log.error(e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            //
            ytSession.setLastTokenRequest(new Date());
            youtubeRepository.save(ytSession);
            youtubeService.setCredential(ytSession);
            log.info("sucesso");
            //
        }

        return new ResponseEntity<>("<script>window.close()</script>", HttpStatus.OK);
    }

    @GetMapping("/channelInfo")
    public ResponseEntity<List<Channel>> getChannelInfo() throws IOException, GeneralSecurityException {
        if(!isConnected())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try
        {
            youtubeService.setCredential(youtubeSession);
            ChannelListResponse response = youtubeService.getChannelInfo();
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

    @GetMapping("/liveBroadcastsList/upcoming")
    public ResponseEntity<LiveBroadcastListResponse> getLiveBroadcastsListUpcoming() throws IOException, GeneralSecurityException {
        if(!isConnected())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try
        {
            youtubeService.setCredential(youtubeSession);
            LiveBroadcastListResponse response = youtubeService.getLiveBroadcastsList();
            return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping("/liveBroadcastsList/{broadcastId}")
    public ResponseEntity<LiveBroadcastListResponse> getLiveBroadcastsById(@PathVariable("broadcastId") String broadcastId) throws IOException, GeneralSecurityException {
        if(!isConnected())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try
        {
            youtubeService.setCredential(youtubeSession);
            LiveBroadcastListResponse response = youtubeService.getLiveBroadcastsList(broadcastId);
            return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping("/liveStreamsList")
    public ResponseEntity<LiveStreamListResponse> getLiveStreamsList() throws IOException, GeneralSecurityException {
        if(!isConnected())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try
        {
            youtubeService.setCredential(youtubeSession);
            LiveStreamListResponse response = youtubeService.getLiveStreamsList();
            return new ResponseEntity<>(response, HttpStatus.OK);
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

    @PostMapping("/liveBroadcast")
    public ResponseEntity<YoutubeLiveBroadcast> postLiveBroadcast(@RequestBody YoutubeLiveBroadcast liveData) throws IOException, GeneralSecurityException {
        if(!isConnected())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try
        {
            youtubeService.setCredential(youtubeSession);
            YoutubeLiveBroadcast response = youtubeService.addLiveBroadcast(liveData);

            return new ResponseEntity<>(response, HttpStatus.OK);
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
