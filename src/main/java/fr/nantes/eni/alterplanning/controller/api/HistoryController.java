package fr.nantes.eni.alterplanning.controller.api;

import fr.nantes.eni.alterplanning.dao.mysql.entity.HistoryEntity;
import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.model.response.HistoryResponse;
import fr.nantes.eni.alterplanning.service.dao.HistoryDAOService;
import fr.nantes.eni.alterplanning.service.dao.UserDAOService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Resource
    private HistoryDAOService historyDAOService;

    @Resource
    private UserDAOService userDAOService;

    @GetMapping("")
    public List<HistoryResponse> getHistories() {
        final List<HistoryEntity> historyEntities = historyDAOService.findAllOrderByDate();
        final List<Integer> idsUser = historyEntities
                .parallelStream()
                .map(HistoryEntity::getUserId)
                .distinct()
                .collect(Collectors.toList());

        final List<UserEntity> userEntities;

        if (!idsUser.isEmpty()) {
            userEntities = userDAOService.findByListIdCours(idsUser);
        } else {
            userEntities = new ArrayList<>();
        }

        return historyEntities.stream().map(h -> {
            final HistoryResponse response = new HistoryResponse();
            response.setId(h.getId());
            response.setCreatedAt(h.getCreatedAt());
            response.setDescription(h.getDescription());
            response.setUser(userEntities
                    .stream()
                    .filter(u -> u.getId() == h.getUserId())
                    .findFirst()
                    .orElse(null));
            return response;
        }).collect(Collectors.toList());
    }
}