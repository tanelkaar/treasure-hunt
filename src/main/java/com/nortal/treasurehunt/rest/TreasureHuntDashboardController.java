package com.nortal.treasurehunt.rest;

import com.nortal.treasurehunt.model.rest.Team;
import com.nortal.treasurehunt.service.TreasureHuntService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games/{gameId}")
public class TreasureHuntDashboardController {

  @Resource
  private TreasureHuntService treasureHuntService;

  @RequestMapping(method = RequestMethod.GET)
  public List<Team> getGameData(@PathVariable Long gameId) {
    return treasureHuntService.getGameData(gameId);
  }

}
