package org.raflab.studsluzbadesktopclient.services;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.raflab.studsluzbadesktopclient.MainView;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Reusable service za generisanje JasperReports izveštaja.
 * Podržava:
 * - Izveštaje sa i bez parametara
 * - Izveštaje sa i bez data source-a
 * - Export u PDF fajl ili prikaz u JasperViewer
 */
@Service
public class ReportService {

    /**
     * Generiše izveštaj i prikazuje ga u JasperViewer-u.
     *
     * @param reportName naziv .jrxml fajla (bez ekstenzije), npr. "sviStudenti"
     * @param parameters mapa parametara za izveštaj (može biti null ili prazna)
     * @param dataSource kolekcija podataka za izveštaj (može biti null za izveštaje bez podataka)
     * @throws ReportException ako dođe do greške pri generisanju
     */
    public void showReport(String reportName, Map<String, Object> parameters, Collection<?> dataSource) 
            throws ReportException {
        JasperPrint jasperPrint = generateReport(reportName, parameters, dataSource);
        JasperViewer.viewReport(jasperPrint, false);
    }

    /**
     * Generiše izveštaj i eksportuje ga u PDF fajl.
     *
     * @param reportName naziv .jrxml fajla (bez ekstenzije), npr. "sviStudenti"
     * @param parameters mapa parametara za izveštaj (može biti null ili prazna)
     * @param dataSource kolekcija podataka za izveštaj (može biti null za izveštaje bez podataka)
     * @param outputPath putanja do izlaznog PDF fajla, npr. "izvestaj.pdf"
     * @throws ReportException ako dođe do greške pri generisanju ili eksportu
     */
    public void exportToPdf(String reportName, Map<String, Object> parameters, 
                           Collection<?> dataSource, String outputPath) throws ReportException {
        JasperPrint jasperPrint = generateReport(reportName, parameters, dataSource);
        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
        } catch (JRException e) {
            throw new ReportException("Greška pri eksportu izveštaja u PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Generiše izveštaj i vraća JasperPrint objekat za dalju manipulaciju.
     *
     * @param reportName naziv .jrxml fajla (bez ekstenzije)
     * @param parameters mapa parametara za izveštaj
     * @param dataSource kolekcija podataka za izveštaj
     * @return JasperPrint objekat
     * @throws ReportException ako dođe do greške
     */
    public JasperPrint generateReport(String reportName, Map<String, Object> parameters, 
                                      Collection<?> dataSource) throws ReportException {
        try {
            InputStream reportStream = MainView.class.getResourceAsStream("/reports/" + reportName + ".jrxml");
            if (reportStream == null) {
                throw new ReportException("Šablon izveštaja nije pronađen: " + reportName + ".jrxml");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Map<String, Object> params = parameters != null ? parameters : new HashMap<>();

            JRDataSource jrDataSource;
            if (dataSource != null && !dataSource.isEmpty()) {
                jrDataSource = new JRBeanCollectionDataSource(dataSource);
            } else {
                jrDataSource = new JREmptyDataSource();
            }

            return JasperFillManager.fillReport(jasperReport, params, jrDataSource);
        } catch (JRException e) {
            throw new ReportException("Greška pri kompajliranju/popunjavanju izveštaja: " + e.getMessage(), e);
        }
    }

    // ===== Convenience metode za uobičajene slučajeve =====

    /**
     * Prikazuje jednostavan izveštaj samo sa podacima (bez parametara).
     */
    public void showReport(String reportName, Collection<?> dataSource) throws ReportException {
        showReport(reportName, null, dataSource);
    }

    /**
     * Prikazuje izveštaj samo sa parametrima (bez data source-a).
     */
    public void showReportWithParams(String reportName, Map<String, Object> parameters) throws ReportException {
        showReport(reportName, parameters, null);
    }

    /**
     * Eksportuje jednostavan izveštaj u PDF (bez parametara).
     */
    public void exportToPdf(String reportName, Collection<?> dataSource, String outputPath) 
            throws ReportException {
        exportToPdf(reportName, null, dataSource, outputPath);
    }

    /**
     * Custom exception za greške pri generisanju izveštaja.
     */
    public static class ReportException extends Exception {
        public ReportException(String message) {
            super(message);
        }

        public ReportException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
