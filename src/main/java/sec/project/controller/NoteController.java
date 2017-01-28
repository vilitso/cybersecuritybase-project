package sec.project.controller;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.h2.tools.RunScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Note;

@Controller
public class NoteController {

    @PostConstruct
    public void init() throws Exception {
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        try {
            RunScript.execute(connection, new FileReader("sql/database-schema.sql"));
            RunScript.execute(connection, new FileReader("sql/database-import.sql"));
        } catch (Throwable t) {
            System.err.println(t.getMessage());
        }

        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Note");
        
        System.out.println("Test database setup:");
        while (resultSet.next()) {
            String nickname = resultSet.getString("nickname");
            String note = resultSet.getString("note");
            System.out.println("New row : \t" + nickname + " " + note);
        } 
        
        resultSet.close();
        connection.close();
    }
       
    private List<Note> noteList;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) throws Exception {
        this.noteList = new ArrayList<>();
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Note");
        
        
        while (resultSet.next()) {
            String nickname = resultSet.getString("nickname");
            String note = resultSet.getString("note");
            
            Note newnote = new Note(nickname, note);
            
            this.noteList.add(newnote);
        } 
        
        resultSet.close();
        connection.close();
        model.addAttribute("notes", this.noteList);
        return "form";
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String nickname, @RequestParam String note)  throws Exception {        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        String sql = "INSERT INTO Note (nickname, note) VALUES ('" + nickname + "', '" + note + "');";
        connection.createStatement().execute(sql);
        System.out.println(sql);  
        return "thankyou";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String loadAdmin(Model model) throws Exception {
        this.noteList = new ArrayList<>();
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Note");
        
        while (resultSet.next()) {
            String nickname = resultSet.getString("nickname");
            String note = resultSet.getString("note");
            
            Note signup = new Note(nickname, note);
            
            this.noteList.add(signup);
        } 
        resultSet.close();
        connection.close();
        model.addAttribute("notes", this.noteList);
        return "admin";
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteNote(@RequestParam String nickname) throws Exception  {
        
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");
        
        String sql = "DELETE FROM Note WHERE nickname = '" + nickname + "'";
        connection.createStatement().execute(sql);        
        connection.close();
        return "redirect:/admin";
    }
}