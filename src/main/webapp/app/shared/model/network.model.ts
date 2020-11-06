export interface INetwork {
  id?: number;
  name?: string;
  type?: string;
  networkParentId?: number;
}

export class Network implements INetwork {
  constructor(public id?: number, public name?: string, public type?: string, public networkParentId?: number) {}
}
