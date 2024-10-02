import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PowiadomieniaDropdownComponent } from './powiadomienia-dropdown.component';

describe('PowiadomieniaDropdownComponent', () => {
  let component: PowiadomieniaDropdownComponent;
  let fixture: ComponentFixture<PowiadomieniaDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PowiadomieniaDropdownComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PowiadomieniaDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
