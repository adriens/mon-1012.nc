/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.mon.annuaire.opt.nc.mobilis.controllers;

import com.github.adriens.mon.annuaire.opt.nc.mobilis.domain.MobilisContact;
import com.github.adriens.mon.annuaire.opt.nc.mobilis.services.MobilisService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author salad74
 */
@RestController
public class MobilisController {
    
    @Autowired
    MobilisService mobilisService;
    
    @RequestMapping(value={"/mobilis/{name}", "/{name}"})
    public List<MobilisContact> getMobilis(
    @PathVariable(value = "name") String name
    ) throws Exception {
        return mobilisService.getMobilis(name);
    }
}
