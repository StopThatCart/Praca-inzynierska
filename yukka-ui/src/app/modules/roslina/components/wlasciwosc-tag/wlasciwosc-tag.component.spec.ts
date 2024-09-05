import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WlasciwoscTagComponent } from './wlasciwosc-tag.component';

describe('WlasciwoscTagComponent', () => {
  let component: WlasciwoscTagComponent;
  let fixture: ComponentFixture<WlasciwoscTagComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WlasciwoscTagComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WlasciwoscTagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
