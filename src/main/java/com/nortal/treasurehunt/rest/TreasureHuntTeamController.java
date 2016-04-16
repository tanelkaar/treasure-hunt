package com.nortal.treasurehunt.rest;

import com.nortal.treasurehunt.model.rest.Link;
import com.nortal.treasurehunt.model.rest.SubmitSolution;
import com.nortal.treasurehunt.model.rest.TeamCurrentState;
import com.nortal.treasurehunt.model.rest.TeamRegistration;
import com.nortal.treasurehunt.service.TreasureHuntService;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games/{gameId}/teams")
public class TreasureHuntTeamController {

  @Resource
  private TreasureHuntService treasureHuntService;

  @RequestMapping(method = RequestMethod.POST)
  public TeamCurrentState registerTeam(@PathVariable Long gameId,
                                       @RequestBody TeamRegistration teamRegistration,
                                       HttpServletRequest request) {

    TeamCurrentState teamCurrentState = treasureHuntService.registerTeam(gameId, teamRegistration);
    if (teamCurrentState.getId() != null) {
      fillLink(teamCurrentState, request.getRequestURL().toString() + "/" + teamCurrentState.getId());
    }
    return teamCurrentState;
  }

  @RequestMapping(value = "/{teamId}", method = RequestMethod.GET)
  public TeamCurrentState getCurrentState(@PathVariable Long gameId,
                                          @PathVariable Long teamId,
                                          @RequestParam(name = "getNewAssignment", required=false) String getNewAssignment,
                                          HttpServletRequest request) {
    TeamCurrentState teamCurrentState = getNewAssignment != null ? treasureHuntService.getNewAssignment(gameId, teamId)
                                                                : treasureHuntService.getCurrentState(gameId, teamId);
    if (teamCurrentState.getId() != null) {
      fillLink(teamCurrentState, request.getRequestURL().toString());
    }
    return teamCurrentState;
  }

  @RequestMapping(value = "/{teamId}", method = RequestMethod.POST)
  public TeamCurrentState submitSolution(@PathVariable Long gameId,
                                          @PathVariable Long teamId,
                                          @RequestBody SubmitSolution submitSolution,
                                          HttpServletRequest request) {
    TeamCurrentState teamCurrentState = treasureHuntService.submitSolution(gameId, teamId, submitSolution);
    if (teamCurrentState.getId() != null) {
      fillLink(teamCurrentState, request.getRequestURL().toString());
    }
    return teamCurrentState;
  }

  private void fillLink(TeamCurrentState teamCurrentState, String url) {
    teamCurrentState.setLinks(Arrays.asList(new Link("self", url)));
  }

}
