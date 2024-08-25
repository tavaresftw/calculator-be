package com.ntd.calculator.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "randomStringClient", url = "https://www.random.org")
public interface RandomStringClient {

    @GetMapping("/strings/?num=1&len={len}&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new")
    String getRandomString(@PathVariable("len") Integer len);

}
