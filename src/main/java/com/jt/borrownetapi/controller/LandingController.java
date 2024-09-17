package com.jt.borrownetapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingController {

    @Value("${info.app.version}")
    String appVersion;

    @GetMapping("/")
    public ResponseEntity<String> homeController() {
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>BorrowNet-API</title>
                </head>
                <body>
                
                <pre>
                
                X▄▄▄▄▄▄▄▄▄▄XXX▄▄▄▄▄▄▄▄▄▄▄XX▄▄▄▄▄▄▄▄▄▄▄XX▄▄▄▄▄▄▄▄▄▄▄XX▄▄▄▄▄▄▄▄▄▄▄XX▄XXXXXXXXX▄XX▄▄XXXXXXXX▄XX▄▄▄▄▄▄▄▄▄▄▄XX▄▄▄▄▄▄▄▄▄▄▄XXXXXXXXXX▄▄▄▄▄▄▄▄▄▄▄XX▄▄▄▄▄▄▄▄▄▄▄XX▄▄▄▄▄▄▄▄▄▄▄X
                ▐░░░░░░░░░░▌X▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░▌XXXXXXX▐░▌▐░░▌XXXXXX▐░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌XXXXXXXX▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌
                ▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░▌XXXXXXX▐░▌▐░▌░▌XXXXX▐░▌▐░█▀▀▀▀▀▀▀▀▀XX▀▀▀▀█░█▀▀▀▀XXXXXXXXX▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌X▀▀▀▀█░█▀▀▀▀X
                ▐░▌XXXXXXX▐░▌▐░▌XXXXXXX▐░▌▐░▌XXXXXXX▐░▌▐░▌XXXXXXX▐░▌▐░▌XXXXXXX▐░▌▐░▌XXXXXXX▐░▌▐░▌▐░▌XXXX▐░▌▐░▌XXXXXXXXXXXXXXX▐░▌XXXXXXXXXXXXX▐░▌XXXXXXX▐░▌▐░▌XXXXXXX▐░▌XXXXX▐░▌XXXXX
                ▐░█▄▄▄▄▄▄▄█░▌▐░▌XXXXXXX▐░▌▐░█▄▄▄▄▄▄▄█░▌▐░█▄▄▄▄▄▄▄█░▌▐░▌XXXXXXX▐░▌▐░▌XXX▄XXX▐░▌▐░▌X▐░▌XXX▐░▌▐░█▄▄▄▄▄▄▄▄▄XXXXXX▐░▌X▄▄▄▄▄▄▄▄▄▄▄X▐░█▄▄▄▄▄▄▄█░▌▐░█▄▄▄▄▄▄▄█░▌XXXXX▐░▌XXXXX
                ▐░░░░░░░░░░▌X▐░▌XXXXXXX▐░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░▌XXXXXXX▐░▌▐░▌XX▐░▌XX▐░▌▐░▌XX▐░▌XX▐░▌▐░░░░░░░░░░░▌XXXXX▐░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌XXXXX▐░▌XXXXX
                ▐░█▀▀▀▀▀▀▀█░▌▐░▌XXXXXXX▐░▌▐░█▀▀▀▀█░█▀▀X▐░█▀▀▀▀█░█▀▀X▐░▌XXXXXXX▐░▌▐░▌X▐░▌░▌X▐░▌▐░▌XXX▐░▌X▐░▌▐░█▀▀▀▀▀▀▀▀▀XXXXXX▐░▌X▀▀▀▀▀▀▀▀▀▀▀X▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀▀▀XXXXXX▐░▌XXXXX
                ▐░▌XXXXXXX▐░▌▐░▌XXXXXXX▐░▌▐░▌XXXXX▐░▌XX▐░▌XXXXX▐░▌XX▐░▌XXXXXXX▐░▌▐░▌▐░▌X▐░▌▐░▌▐░▌XXXX▐░▌▐░▌▐░▌XXXXXXXXXXXXXXX▐░▌XXXXXXXXXXXXX▐░▌XXXXXXX▐░▌▐░▌XXXXXXXXXXXXXXX▐░▌XXXXX
                ▐░█▄▄▄▄▄▄▄█░▌▐░█▄▄▄▄▄▄▄█░▌▐░▌XXXXXX▐░▌X▐░▌XXXXXX▐░▌X▐░█▄▄▄▄▄▄▄█░▌▐░▌░▌XXX▐░▐░▌▐░▌XXXXX▐░▐░▌▐░█▄▄▄▄▄▄▄▄▄XXXXXX▐░▌XXXXXXXXXXXXX▐░▌XXXXXXX▐░▌▐░▌XXXXXXXXXXX▄▄▄▄█░█▄▄▄▄X
                ▐░░░░░░░░░░▌X▐░░░░░░░░░░░▌▐░▌XXXXXXX▐░▌▐░▌XXXXXXX▐░▌▐░░░░░░░░░░░▌▐░░▌XXXXX▐░░▌▐░▌XXXXXX▐░░▌▐░░░░░░░░░░░▌XXXXX▐░▌XXXXXXXXXXXXX▐░▌XXXXXXX▐░▌▐░▌XXXXXXXXXX▐░░░░░░░░░░░▌
                X▀▀▀▀▀▀▀▀▀▀XXX▀▀▀▀▀▀▀▀▀▀▀XX▀XXXXXXXXX▀XX▀XXXXXXXXX▀XX▀▀▀▀▀▀▀▀▀▀▀XX▀▀XXXXXXX▀▀XX▀XXXXXXXX▀▀XX▀▀▀▀▀▀▀▀▀▀▀XXXXXXX▀XXXXXXXXXXXXXXX▀XXXXXXXXX▀XX▀XXXXXXXXXXXX▀▀▀▀▀▀▀▀▀▀▀X
                XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                </pre>
                <p>Version %s</p>
                
                </body>
                </html>
                """;
        return new ResponseEntity<>( html.formatted(appVersion), HttpStatusCode.valueOf(200));
    }
}
