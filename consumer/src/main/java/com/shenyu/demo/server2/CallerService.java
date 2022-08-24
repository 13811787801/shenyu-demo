package com.shenyu.demo.server2;

import com.shenyu.demo.server2.feignClient.ConnectFeignClient;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Import(FeignClientsConfiguration.class)
public class CallerService {

    private ConnectFeignClient connectFeignClient;

    @Autowired
    public CallerService(Client client, Contract contract, Decoder decoder, Encoder encoder) {
        connectFeignClient = Feign.builder()
                .client(client)
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
//                .target(new Target.HardCodedTarget<>(ConnectFeignClient.class, "http://server1"));
                .target(Target.EmptyTarget.create(ConnectFeignClient.class));
    }

    public <T> T requestConnect(String url, String aaa) {
        return (T) connectFeignClient.requestConnect(URI.create(url), aaa);
    }

}
