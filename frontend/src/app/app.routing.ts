import { RouterModule } from "@angular/router";
import { ContactComponent } from "./components/contact/contact.component";
import { IndexComponent } from "./components/index/index.component";

const appRoutes = [
    { path: '', component: IndexComponent },
    { path: 'index', component: IndexComponent },
    { path: 'contact', component: ContactComponent}
]

export const routing = RouterModule.forRoot(appRoutes);