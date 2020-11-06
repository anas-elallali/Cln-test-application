import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INetwork } from 'app/shared/model/network.model';
import { NetworkService } from './network.service';

@Component({
  templateUrl: './network-delete-dialog.component.html',
})
export class NetworkDeleteDialogComponent {
  network?: INetwork;

  constructor(protected networkService: NetworkService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.networkService.delete(id).subscribe(() => {
      this.eventManager.broadcast('networkListModification');
      this.activeModal.close();
    });
  }
}
