import {Team} from './Team.model'
export interface Match {
    id :number;
    team1 : Team;
    team2 : Team;
    score1: number;
    score2: number;
    isPlayed : boolean;
}
	