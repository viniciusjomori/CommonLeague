package br.com.jrr.apiTest.Notification;

import br.com.jrr.apiTest.Notification.DTO.NotificationDTO;
import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.Tournament.Entity.TournamentJoinEntity;
import br.com.jrr.apiTest.Tournament.Enum.TournamentJoinStatus;
import br.com.jrr.apiTest.Tournament.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        //Tournament update notification
        if(this.teamService.getCurrentTeam().isPresent()){
            Collection<TournamentJoinEntity> joins = tournamentService.findJoinsByCurrentTeam();

            for (TournamentJoinEntity join : joins) {
                NotificationType type = notificationTypeByTournamentStatus(join.getStatus());
                LocalDateTime date = join.getUpdateDate() != null ? join.getUpdateDate() : join.getCreationDate();
                notifications.add(new NotificationDTO(type, join.getId(), null, "tournamentEntity", date));

            }

        //Team invitation notifications
        } else {
            Collection<TeamJoinEntity> teamInvites = teamService.findPendingByCurrentUser();

            for(TeamJoinEntity teamInvite : teamInvites){
                LocalDateTime date = teamInvite.getCreationDate();
                notifications.add(new NotificationDTO(NotificationType.TEAM_INVITE, teamInvite.getId(), teamInvite.getTeam().getName(),  "teamJoinEntity", date));
            }
        }

        return notifications;
    }

    private static NotificationType notificationTypeByTournamentStatus(TournamentJoinStatus status) {
        NotificationType type;

        switch (status){
            case WAITING_TOURNAMENT -> type = NotificationType.TOURNAMENT_ENTRY;
            case LEFT -> type = NotificationType.TOURNAMENT_LEFT;
            case PLAYING -> type = NotificationType.TOURNAMENT_PLAYING;
            case WAITING_ROUND -> type = NotificationType.TOURNAMENT_WAITING_ROUND;
            case WIN -> type = NotificationType.TOURNAMENT_WIN;
            case LOSE -> type = NotificationType.TOURNAMENT_LOSE;
            default -> throw new IllegalArgumentException("Invalid tournament status");
        }
        return type;
    }
}
