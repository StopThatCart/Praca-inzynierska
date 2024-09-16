import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RozmowaCardComponent } from './rozmowa-card.component';

describe('RozmowaCardComponent', () => {
  let component: RozmowaCardComponent;
  let fixture: ComponentFixture<RozmowaCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RozmowaCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RozmowaCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
