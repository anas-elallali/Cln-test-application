import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ClnTestTestModule } from '../../../test.module';
import { NetworkUpdateComponent } from 'app/entities/network/network-update.component';
import { NetworkService } from 'app/entities/network/network.service';
import { Network } from 'app/shared/model/network.model';

describe('Component Tests', () => {
  describe('Network Management Update Component', () => {
    let comp: NetworkUpdateComponent;
    let fixture: ComponentFixture<NetworkUpdateComponent>;
    let service: NetworkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ClnTestTestModule],
        declarations: [NetworkUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(NetworkUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NetworkUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NetworkService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Network(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Network();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
