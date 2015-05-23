package com.rlogin.service.impl;

import com.rlogin.domain.gjj.GjjAsynDomain;
import com.rlogin.service.GjjService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 异步解析数据线程
 * Created by changxx on 15/5/22.
 */
public class GjjAsynRecorder implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GjjAsynRecorder.class);

    private GjjServiceImpl gjjServiceImpl;


    GjjAsynRecorder(GjjServiceImpl gjjServiceImpl) {
        this.gjjServiceImpl = gjjServiceImpl;
    }

    @Override
    public void run() {
        while (true) {
            try {
                GjjAsynDomain gjjAsynDomain = gjjServiceImpl.getGjjAsynDomainQuery().take();

                // 记录公积金明细
                gjjServiceImpl.recordGjjDetail(gjjAsynDomain.getCookie(), gjjAsynDomain.getGjjUser());

                // 记录贷款明细
                gjjServiceImpl.recordLoanDetail(gjjAsynDomain.getCookie(), gjjAsynDomain.getGjjUser());

                // 纪录贷款信息
                gjjServiceImpl.recordLoan(gjjAsynDomain.getCookie(), gjjAsynDomain.getGjjUser());
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }

    }
}
