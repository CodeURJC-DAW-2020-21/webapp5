import { NgModule } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { routing } from './app.routing';
import { AppComponent } from './app.component';
import { IndexComponent } from './components/index/index.component';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { ContactComponent } from './components/contact/contact.component';
import { CarouselComponent } from './components/index/carousel/carousel.component';
import { TournamentsComponent } from './components/tournaments/tournaments.component';
import { LoginComponent } from './components/login/login.component';
import { TeamsComponent } from './components/teams/teams.component';
import { TCollapse } from './components/tournaments/collapse/collapse.component';
import { NewTeamComponent } from './components/teams/newTeam/newTeam.component';
import { TeamComponent } from './components/teams/team/team.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SettingsComponent } from './components/settings/settings.component';
import { NewTournamentComponent } from "./components/tournaments/newTournament/newTournament.component"
import { TournamentComponent } from './components/tournaments/tournament/tournament.component';
import { TNav } from './components/tournaments/tournament/nav/nav.component';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    IndexComponent,
    ContactComponent,
    CarouselComponent,
    TournamentsComponent,
    TCollapse,    
    LoginComponent,
    TeamsComponent,
    TeamComponent,
    NewTeamComponent,
    ProfileComponent,
    SettingsComponent,
    NewTournamentComponent,
    TournamentComponent,
    TNav
  ],
  imports: [
    BrowserModule,
    routing,
    NgbModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
