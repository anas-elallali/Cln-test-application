import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClnTestTestModule } from '../../../test.module';
import { NetworkDetailComponent } from 'app/entities/network/network-detail.component';
import { Network } from 'app/shared/model/network.model';

describe('Component Tests', () => {
  describe('Network Management Detail Component', () => {
    let comp: NetworkDetailComponent;
    let fixture: ComponentFixture<NetworkDetailComponent>;
    const route = ({ data: of({ network: new Network(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ClnTestTestModule],
        declarations: [NetworkDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(NetworkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NetworkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load network on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.network).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
