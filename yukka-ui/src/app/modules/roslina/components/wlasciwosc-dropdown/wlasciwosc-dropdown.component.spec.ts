import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WlasciwoscDropdownComponent } from './wlasciwosc-dropdown.component';

describe('WlasciwoscDropdownComponent', () => {
  let component: WlasciwoscDropdownComponent;
  let fixture: ComponentFixture<WlasciwoscDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WlasciwoscDropdownComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WlasciwoscDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
