/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.mon.annuaire.opt.nc.mobilis.services;

import com.github.adriens.mon.annuaire.opt.nc.mobilis.domain.MobilisContact;
import com.github.adriens.mon.annuaire.opt.nc.mobilis.sdk.OPTCrawler;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author salad74
 */
@Service
public class MobilisService {
    private final Logger log = LoggerFactory.getLogger(MobilisService.class);
    
    public List<MobilisContact> getMobilis(String aName) throws Exception {
        return OPTCrawler.searchMobilis(aName);
    }
}
