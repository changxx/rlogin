package com.rlogin.service.impl;

import com.rlogin.service.GjjService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步解析数据线程
 * Created by changxx on 15/5/22.
 */
public class GjjAsynRecorder implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GjjAsynRecorder.class);

    private GjjService gjjService;


    GjjAsynRecorder(GjjService gjjService) {
        this.gjjService = gjjService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                gjjService.record();
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }

    }
}
