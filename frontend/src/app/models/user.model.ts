import {Team} from './team.model'

export interface User {
    id?: number;
    name: string ;
    email: string ;
    encodedPassword: string ;
    image : boolean;
    team : Team; 
    roles : string[];
    riot : string;
    blizzard : string;
    psn : string;
    xbox : string;
    steam : string;
}
	