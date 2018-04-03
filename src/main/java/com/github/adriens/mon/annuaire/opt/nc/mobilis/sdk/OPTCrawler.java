/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.mon.annuaire.opt.nc.mobilis.sdk;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.github.adriens.mon.annuaire.opt.nc.mobilis.domain.MobilisContact;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author salad74
 */
public class OPTCrawler {

    public static final String URL_ROOT = "http://1012.nc/";

    final static Logger logger = LoggerFactory.getLogger(OPTCrawler.class);

    private static WebClient buildWebClient() {
        // Disable verbose logs
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
        WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        return webClient;
    }

    public final static void fillUp(String text) throws IOException {
        WebClient webClient = buildWebClient();

        HtmlPage htmlPage = webClient.getPage(OPTCrawler.URL_ROOT);

        HtmlForm htmlForm = htmlPage.getFormByName("saisie");
        // get the input field
        HtmlInput input = (HtmlInput) htmlForm.getInputByName("search");

        input.setValueAttribute(text);
        HtmlSubmitInput submit = htmlPage.getElementByName("Submit");

        HtmlPage recherchePage = submit.click();

        //logger.info(recherchePage.asXml());
        // get the div pages blanches
        HtmlDivision divResultsPagesBlanches = recherchePage.getFirstByXPath("/html/body/div[1]/div/div/div[4]/div/div[3]");
        DomNodeList tablesBlanches = divResultsPagesBlanches.getElementsByTagName("table");
        logger.info("Found <" + tablesBlanches.size() + "> pages blanches");
        Iterator<DomNode> tableBlanchesIter = tablesBlanches.iterator();
        HtmlTable lTable;
        int i = 0;

        while (tableBlanchesIter.hasNext()) {
            lTable = (HtmlTable) tableBlanchesIter.next();

            String nomComplet = lTable.getCellAt(0, 0).getFirstChild().getTextContent();
            logger.info("Nom : <" + nomComplet + ">");
            String ville = lTable.getCellAt(0, 1).getFirstChild().getTextContent();
            logger.info("Ville : <" + ville + ">");

            String adresse = lTable.getCellAt(1, 0).getTextContent();
            logger.info("Adresse : <" + adresse + ">");

            //fetch table rows
            logger.debug("Fetching table <" + i + ">");
            HtmlTableBody tableBody = lTable.getBodies().get(0);
            List<HtmlTableRow> rows = tableBody.getRows();
            logger.debug("Found <" + rows.size() + "> rows");
            for (HtmlTableRow row : tableBody.getRows()) {
                //logger.info(row.getCell(0).asXml());
                //logger.info(row.getCell(0).getFirstChild().getTextContent());
                // looking for telephone
                logger.debug("Telephone : " + row.getCell(0).getFirstChild().getAttributes().getNamedItem("title"));
                String tmp = row.getCell(0).getFirstChild().getAttributes().getNamedItem("title") + "";
                if (tmp.contains("Telephone")) {
                    String telephone = row.getCell(1).getFirstChild().getTextContent();
                    logger.info("Found tel : <" + telephone + ">");
                }
            }
            i++;
            logger.info("#########################################################");

        }

        // get the div pages jaunes
        HtmlDivision divResultsPagesJaunes = recherchePage.getFirstByXPath("//*[@id=\"result_j\"]");
        DomNodeList tablesJaunes = divResultsPagesJaunes.getElementsByTagName("table");
        logger.info("Found <" + tablesJaunes.size() + "> pages jaunes");
        Iterator<DomNode> tableJaunesIter = tablesJaunes.iterator();
        while (tableJaunesIter.hasNext()) {
            lTable = (HtmlTable) tableJaunesIter.next();
            String nomComplet = lTable.getCellAt(1, 0).getFirstChild().getTextContent();
            logger.info("Jaune::Nom : <" + nomComplet + ">");
        }

        //////////////////////////////////////////////////////////////////////////:
        // mobilis
        HtmlDivision divResultsPagesMobilis = recherchePage.getFirstByXPath("/html/body/div[1]/div/div/div[6]/div/div[3]");
        DomNodeList tablesMoilis = divResultsPagesMobilis.getElementsByTagName("table");
        logger.info("Found <" + tablesMoilis.size() + "> pages mobilis");

        Iterator<DomNode> tableMobilisIter = tablesMoilis.iterator();
        while (tableMobilisIter.hasNext()) {
            lTable = (HtmlTable) tableMobilisIter.next();
            String nomComplet = lTable.getCellAt(0, 0).getFirstChild().getTextContent();
            logger.info("Mobilis::Nom : <" + nomComplet + ">");
            String mobilis = lTable.getCellAt(0, 1).getFirstChild().getTextContent();
            logger.info("Mobilis : <" + mobilis + ">");
            logger.info("#########################################################");
        }

    }

    public static final List<MobilisContact> searchMobilis(String name) throws IOException {
        ArrayList<MobilisContact> out = new ArrayList<>();
        WebClient webClient = buildWebClient();

        HtmlPage htmlPage = webClient.getPage(OPTCrawler.URL_ROOT);

        HtmlForm htmlForm = htmlPage.getFormByName("saisie");
        // get the input field
        HtmlInput input = (HtmlInput) htmlForm.getInputByName("search");

        input.setValueAttribute(name);
        HtmlSubmitInput submit = htmlPage.getElementByName("Submit");

        HtmlPage recherchePage = submit.click();

        HtmlDivision divResultsPagesMobilis = recherchePage.getFirstByXPath("/html/body/div[1]/div/div/div[6]/div/div[3]");
        DomNodeList tablesMoilis = divResultsPagesMobilis.getElementsByTagName("table");
        logger.info("Found <" + tablesMoilis.size() + "> pages mobilis");
        HtmlTable lTable;
        Iterator<DomNode> tableMobilisIter = tablesMoilis.iterator();
        while (tableMobilisIter.hasNext()) {
            lTable = (HtmlTable) tableMobilisIter.next();
            String nomComplet = lTable.getCellAt(0, 0).getFirstChild().getTextContent().trim();
            logger.info("Mobilis::Nom : <" + nomComplet + ">");
            String mobilis = lTable.getCellAt(0, 1).getFirstChild().getTextContent();
            logger.info("Mobilis : <" + mobilis + ">");
            
            MobilisContact contact = new MobilisContact(nomComplet, mobilis);
            logger.info("Adding new mobilis contact : <" + contact + ">");
            out.add(contact);
            logger.info("#########################################################");
        }
        return out;
    }

    public static void main(String[] args) {
        try {
            String texte = "marine";
            OPTCrawler.searchMobilis(texte);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

}
