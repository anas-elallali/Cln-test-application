import { IProfil } from 'app/shared/model/profil.model';

export interface IRole {
  id?: number;
  name?: string;
  profils?: IProfil[];
}

export class Role implements IRole {
  constructor(public id?: number, public name?: string, public profils?: IProfil[]) {}
}
