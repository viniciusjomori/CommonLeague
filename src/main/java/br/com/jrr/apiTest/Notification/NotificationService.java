package br.com.jrr.apiTest.Notification;

import br.com.jrr.apiTest.Notification.DTO.NotificationDTO;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TournamentService tournamentService;
    public List<NotificationDTO> getCurrentUserNotifications(){
        TournamentEntity currentTournament = tournamentService.getCurrentActiveJoin().getTournament();
        Collection<TeamJoinEntity> teamInvites = teamService.findPendingByCurrentUser();

        List<NotificationDTO> notifications = new ArrayList<>();

        //TODO check if user is on a team, if not, check team invites
        //"Your team has been registered for a tournament" notification
        notifications.add(new NotificationDTO(NotificationType.TOURNAMENT_ENTRY, currentTournament.getId()));

        //"You've been invited for a team" notifications
        for(TeamJoinEntity teamInvite : teamInvites){
            notifications.add(new NotificationDTO(NotificationType.TEAM_INVITE, teamInvite.getId()));
        }

        return notifications;
    }
}
