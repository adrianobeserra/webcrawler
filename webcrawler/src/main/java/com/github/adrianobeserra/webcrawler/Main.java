/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adrianobeserra.webcrawler;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Adriano
 */
public class Main {

    public static DB db = new DB();

    public static void main(String[] args) throws SQLException, IOException {
        db.runSql2("TRUNCATE Record;");
        processPage("http://www.mit.edu");
    }

    public static void processPage(String URL) throws SQLException, IOException {
        String sql = "select * from Record where URL = '" + URL + "'";
        ResultSet rs = db.runSql(sql);
        if (rs.next()) {

        } else {
            sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
            PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, URL);
            stmt.execute();

            Document doc = Jsoup.connect("http://www.mit.edu/").get();

            if (doc.text().contains("research")) {
                System.out.println(URL);
            }

            Elements questions = doc.select("a[href]");
            for (Element link : questions) {
                if (link.attr("href").contains("mit.edu")) {
                    processPage(link.attr("abs:href"));
                }
            }
        }
    }
}

