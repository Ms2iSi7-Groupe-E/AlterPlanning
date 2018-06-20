package fr.nantes.eni.alterplanning.util;

import fr.nantes.eni.alterplanning.dao.mysql.entity.HistoryEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.service.dao.HistoryDAOService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Component
public class HistoryUtil {

    @Resource
    private HistoryDAOService historyDAOService;

    public void addLine(@NotNull final String description) {
        final UserEntity userFromToken = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.setDescription(description);
        historyEntity.setUserId(userFromToken.getId());
        historyDAOService.create(historyEntity);
    }
}
