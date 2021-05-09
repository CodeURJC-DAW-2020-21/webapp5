import { RouterModule } from "@angular/router";
import { ContactComponent } from "./components/contact/contact.component";
import { IndexComponent } from "./components/index/index.component";
import { TournamentsComponent } from "./components/tournaments/tournaments.component";
import { LoginComponent } from "./components/login/login.component";
import { TeamsComponent } from "./components/teams/teams.component";
import { NewTeamComponent } from "./components/teams/newTeam/newTeam.component";
import { TeamComponent } from "./components/teams/team/team.component";
import { ProfileComponent } from "./components/profile/profile.component";
import { SettingsComponent } from "./components/settings/settings.component";
import { NewTournamentComponent } from "./components/tournaments/newTournament/newTournament.component";
import { TournamentComponent } from "./components/tournaments/tournament/tournament.component";

const appRoutes = [
    { path: '', component: IndexComponent},
    { path: 'index', component: IndexComponent},
    { path: 'contact', component: ContactComponent},
    { path: 'tournaments', component: TournamentsComponent},
    { path: 'login', component: LoginComponent},
    { path: 'teams', component: TeamsComponent},
    { path: 'teams/:id', component: TeamComponent},
    { path: 'newTeam', component: NewTeamComponent},
    { path: 'user/:name', component: ProfileComponent},
    { path: 'user/:name/settings', component: SettingsComponent},
    { path: 'newTournament', component: NewTournamentComponent},
    { path: 'tournaments/:id', component: TournamentComponent}
]

export const routing = RouterModule.forRoot(appRoutes);