package fr.nantes.eni.alterplanning.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.util.AlterDateUtil;

import java.util.Date;

/**
 * Created by ughostephan on 23/06/2017.
 */
public class HistoryResponse {

    private int id;

    private UserEntity user;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlterDateUtil.ddMMyyyyHHmmss, timezone=AlterDateUtil.timezone)
    private Date createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
