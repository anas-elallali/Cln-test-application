import { Moment } from 'moment';
import { IRole } from 'app/shared/model/role.model';

export interface IProfil {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  birthDay?: Moment;
  userId?: number;
  networkId?: number;
  roles?: IRole[];
}

export class Profil implements IProfil {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public birthDay?: Moment,
    public userId?: number,
    public networkId?: number,
    public roles?: IRole[]
  ) {}
}
