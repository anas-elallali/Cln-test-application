import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { INetwork, Network } from 'app/shared/model/network.model';
import { NetworkService } from './network.service';

@Component({
  selector: 'jhi-network-update',
  templateUrl: './network-update.component.html',
})
export class NetworkUpdateComponent implements OnInit {
  isSaving = false;
  networks: INetwork[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    type: [],
    networkParentId: [],
  });

  constructor(protected networkService: NetworkService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ network }) => {
      this.updateForm(network);

      this.networkService.query().subscribe((res: HttpResponse<INetwork[]>) => (this.networks = res.body || []));
    });
  }

  updateForm(network: INetwork): void {
    this.editForm.patchValue({
      id: network.id,
      name: network.name,
      type: network.type,
      networkParentId: network.networkParentId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const network = this.createFromForm();
    if (network.id !== undefined) {
      this.subscribeToSaveResponse(this.networkService.update(network));
    } else {
      this.subscribeToSaveResponse(this.networkService.create(network));
    }
  }

  private createFromForm(): INetwork {
    return {
      ...new Network(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
      networkParentId: this.editForm.get(['networkParentId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INetwork>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: INetwork): any {
    return item.id;
  }
}
