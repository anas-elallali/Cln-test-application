import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INetwork } from 'app/shared/model/network.model';

@Component({
  selector: 'jhi-network-detail',
  templateUrl: './network-detail.component.html',
})
export class NetworkDetailComponent implements OnInit {
  network: INetwork | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ network }) => (this.network = network));
  }

  previousState(): void {
    window.history.back();
  }
}
