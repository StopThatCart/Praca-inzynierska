
package com.example.yukka.auth.authorities;

/**
* <ul>
* <li>Admin - Reprezentuje administratora z pełnym dostępem.</li>
* <li>Pracownik - Reprezentuje pracownika z ograniczonym dostępem.</li>
* <li>Uzytkownik - Reprezentuje zwykłego użytkownika z podstawowym dostępem.</li>
* </ul>
*/
public enum ROLE {
    Admin,
    Pracownik,
    Uzytkownik
}
