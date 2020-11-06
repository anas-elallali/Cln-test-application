import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ClnTestTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { NetworkDeleteDialogComponent } from 'app/entities/network/network-delete-dialog.component';
import { NetworkService } from 'app/entities/network/network.service';

describe('Component Tests', () => {
  describe('Network Management Delete Component', () => {
    let comp: NetworkDeleteDialogComponent;
    let fixture: ComponentFixture<NetworkDeleteDialogComponent>;
    let service: NetworkService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ClnTestTestModule],
        declarations: [NetworkDeleteDialogComponent],
      })
        .overrideTemplate(NetworkDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NetworkDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NetworkService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
