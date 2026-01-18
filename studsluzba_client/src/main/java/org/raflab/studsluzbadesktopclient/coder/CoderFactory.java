package org.raflab.studsluzbadesktopclient.coder;

import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class CoderFactory {

    private Coder<SimpleCode> drzaveCoder;
    private Coder<SimpleCode> mestaCoder;
    private Coder<SimpleCode> tipSrednjeSkole;
    private Coder<SimpleCode> oznakaPlana;
    private Coder<SimpleCode> ispitniRok;
    private Coder<SimpleCode> nastavnoZvanje;

    // ucita samo ako nije ucitano
    public Coder<SimpleCode> getSimpleCoder(CoderType coderType){

        switch(coderType) {

            case DRZAVA:
                if(drzaveCoder==null)
                    drzaveCoder = new TextFileSimpleCoder(coderType.getPath());
                return drzaveCoder;
            case MESTO:
                if(mestaCoder==null)
                    mestaCoder = new TextFileSimpleCoder(coderType.getPath());
                return mestaCoder;
            case TIP_SREDNJE_SKOLE:
                if(tipSrednjeSkole==null)
                    tipSrednjeSkole = new TextFileSimpleCoder(coderType.getPath());
                return tipSrednjeSkole;
            case OZNAKA_PLANA:
                if(oznakaPlana==null)
                    oznakaPlana = new TextFileSimpleCoder(coderType.getPath());
                return oznakaPlana;
            case ISPITNI_ROK:
                if(ispitniRok==null)
                    ispitniRok = new TextFileSimpleCoder(coderType.getPath());
                return ispitniRok;
            case NASTAVNO_ZVANJE:
                if(nastavnoZvanje==null)
                    nastavnoZvanje = new TextFileSimpleCoder(coderType.getPath());
                return nastavnoZvanje;
            default: return null;
        }
    }
}
