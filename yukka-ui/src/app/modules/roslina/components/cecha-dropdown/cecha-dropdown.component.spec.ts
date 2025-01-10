import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CechaDropdownComponent } from './cecha-dropdown.component';

describe('CechaDropdownComponent', () => {
  let component: CechaDropdownComponent;
  let fixture: ComponentFixture<CechaDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CechaDropdownComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CechaDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
