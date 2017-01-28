package sec.project.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Note extends AbstractPersistable<Long> {

    private String nickname;
    private String note;
    
    public Note() {
        super();
    }
    
    public Note(String nickname, String note) {
        this();
        this.nickname = nickname;
        this.note = note;
    }
    
    public Note(String nickname) {
        this();
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
}
