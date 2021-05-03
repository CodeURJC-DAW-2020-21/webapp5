import { RouterModule } from "@angular/router";
import { ContactComponent } from "./components/contact/contact.component";
import { IndexComponent } from "./components/index/index.component";
import { TournamentsComponent } from "./components/tournaments/tournament.component";

const appRoutes = [
    { path: '', component: IndexComponent },
    { path: 'index', component: IndexComponent },
    { path: 'contact', component: ContactComponent},
    { path: 'tournaments', component: TournamentsComponent}
]

export const routing = RouterModule.forRoot(appRoutes);