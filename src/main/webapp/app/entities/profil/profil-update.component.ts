import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProfil, Profil } from 'app/shared/model/profil.model';
import { ProfilService } from './profil.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { INetwork } from 'app/shared/model/network.model';
import { NetworkService } from 'app/entities/network/network.service';

type SelectableEntity = IUser | INetwork;

@Component({
  selector: 'jhi-profil-update',
  templateUrl: './profil-update.component.html',
})
export class ProfilUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  networks: INetwork[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    phoneNumber: [],
    birthDay: [],
    userId: [],
    networkId: [],
  });

  constructor(
    protected profilService: ProfilService,
    protected userService: UserService,
    protected networkService: NetworkService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profil }) => {
      if (!profil.id) {
        const today = moment().startOf('day');
        profil.birthDay = today;
      }

      this.updateForm(profil);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.networkService.query().subscribe((res: HttpResponse<INetwork[]>) => (this.networks = res.body || []));
    });
  }

  updateForm(profil: IProfil): void {
    this.editForm.patchValue({
      id: profil.id,
      firstName: profil.firstName,
      lastName: profil.lastName,
      email: profil.email,
      phoneNumber: profil.phoneNumber,
      birthDay: profil.birthDay ? profil.birthDay.format(DATE_TIME_FORMAT) : null,
      userId: profil.userId,
      networkId: profil.networkId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profil = this.createFromForm();
    if (profil.id !== undefined) {
      this.subscribeToSaveResponse(this.profilService.update(profil));
    } else {
      this.subscribeToSaveResponse(this.profilService.create(profil));
    }
  }

  private createFromForm(): IProfil {
    return {
      ...new Profil(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      birthDay: this.editForm.get(['birthDay'])!.value ? moment(this.editForm.get(['birthDay'])!.value, DATE_TIME_FORMAT) : undefined,
      userId: this.editForm.get(['userId'])!.value,
      networkId: this.editForm.get(['networkId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfil>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
