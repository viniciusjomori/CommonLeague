package br.com.jrr.apiTest.Notification;

import br.com.jrr.apiTest.Notification.DTO.NotificationDTO;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.Tournament.Entity.TournamentEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentStatus;
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
        List<NotificationDTO> notifications = new ArrayList<>();

        if(this.teamService.getCurrentTeam().isPresent()){
            TournamentEntity currentTournament = tournamentService.getCurrentActiveJoin().getTournament();
            NotificationType notificationType = notificationTypeByTournamentStatus(currentTournament.getStatus());

            //Tournament update notification
            notifications.add(new NotificationDTO(notificationType, currentTournament.getId(), "tournamentEntity"));
        }else{
            Collection<TeamJoinEntity> teamInvites = teamService.findPendingByCurrentUser();

            for(TeamJoinEntity teamInvite : teamInvites){
                //Team invitation notifications
                notifications.add(new NotificationDTO(NotificationType.TEAM_INVITE, teamInvite.getId(), "teamJoinEntity"));
            }
        }

        return notifications;
    }

    private static NotificationType notificationTypeByTournamentStatus(TournamentStatus currentTournament) {
        NotificationType type;

        switch (currentTournament){
            case PENDING -> type = NotificationType.TOURNAMENT_ENTRY;
            case IN_PROGRESS -> type = NotificationType.TOURNAMENT_PLAYING;
            case FINISHED -> type = NotificationType.TOURNAMENT_FINISHED;
            case CANCELLED -> type = NotificationType.TOURNAMENT_LEFT;
            default -> throw new IllegalArgumentException("Invalid tournament status");
        }
        return type;
    }
}
