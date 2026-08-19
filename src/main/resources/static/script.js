const universityInput =
    document.getElementById("university");

const universityIdInput =
    document.getElementById("universityId");

const suggestions =
    document.getElementById("suggestions");

const form =
    document.getElementById("registrationForm");

const message =
    document.getElementById("message");

const registerButton =
    document.getElementById("registerButton");

let timeout = null;

let currentRequest = null;


// ======================================
// ПОИСК ВУЗОВ
// ======================================

universityInput.addEventListener(
    "input",
    function () {

        const query =
            universityInput.value.trim();

        universityIdInput.value = "";

        clearTimeout(timeout);

        if (query.length < 2) {

            hideSuggestions();

            return;
        }

        timeout = setTimeout(
            () => searchUniversities(query),
            350
        );
    }
);


// ======================================
// ЗАПРОС НА BACKEND
// ======================================

async function searchUniversities(query) {

    try {

        if (currentRequest) {
            currentRequest.abort();
        }

        currentRequest =
            new AbortController();

        const response =
            await fetch(
                `/api/universities/search?query=${encodeURIComponent(query)}`,
                {
                    signal:
                    currentRequest.signal
                }
            );

        if (!response.ok) {

            throw new Error(
                "Ошибка поиска ВУЗов"
            );
        }

        const universities =
            await response.json();

        showSuggestions(universities);

    } catch (error) {

        if (error.name !== "AbortError") {

            console.error(error);

            hideSuggestions();
        }
    }
}


// ======================================
// ПОКАЗ ПОДСКАЗОК
// ======================================

function showSuggestions(universities) {

    suggestions.innerHTML = "";

    if (
        !universities ||
        universities.length === 0
    ) {

        hideSuggestions();

        return;
    }

    universities.forEach(
        university => {

            const item =
                document.createElement("div");

            item.className =
                "suggestion";

            const name =
                document.createElement("div");

            name.className =
                "suggestion-name";

            name.textContent =
                university.name;

            const address =
                document.createElement("div");

            address.className =
                "suggestion-address";

            address.textContent =
                university.address ||
                university.fullName ||
                "Адрес не указан";

            item.appendChild(name);

            item.appendChild(address);

            item.addEventListener(
                "click",
                () => {

                    universityInput.value =
                        university.name;

                    universityIdInput.value =
                        university.id;

                    hideSuggestions();
                }
            );

            suggestions.appendChild(item);
        }
    );

    suggestions.style.display =
        "block";
}


// ======================================
// СКРЫТЬ ПОДСКАЗКИ
// ======================================

function hideSuggestions() {

    suggestions.style.display =
        "none";
}


// ======================================
// КЛИК ВНЕ ПОДСКАЗОК
// ======================================

document.addEventListener(
    "click",
    function (event) {

        if (
            !event.target.closest(
                ".university-field"
            )
        ) {

            hideSuggestions();
        }
    }
);


// ======================================
// РЕГИСТРАЦИЯ
// ======================================

form.addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        clearMessage();

        const firstName =
            document
                .getElementById("firstName")
                .value
                .trim();

        const lastName =
            document
                .getElementById("lastName")
                .value
                .trim();

        const email =
            document
                .getElementById("email")
                .value
                .trim();

        const university =
            universityInput
                .value
                .trim();

        const universityId =
            universityIdInput
                .value
                .trim();


        if (!firstName ||
            !lastName ||
            !email ||
            !university) {

            showError(
                "Заполните все поля"
            );

            return;
        }


        registerButton.disabled =
            true;

        registerButton.textContent =
            "Регистрация...";


        try {

            const response =
                await fetch(
                    "/api/students",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            firstName,
                            lastName,
                            email,
                            university,
                            universityId
                        })
                    }
                );


            const data =
                await response.json();


            if (!response.ok) {

                throw new Error(
                    typeof data === "string"
                        ? data
                        : "Не удалось зарегистрироваться"
                );
            }


            showSuccess(
                `Регистрация успешна! Ваш ID: ${data.id}`
            );


            form.reset();

            universityIdInput.value = "";

            hideSuggestions();


        } catch (error) {

            showError(
                error.message
            );

        } finally {

            registerButton.disabled =
                false;

            registerButton.textContent =
                "Зарегистрироваться";
        }
    }
);


// ======================================
// СООБЩЕНИЯ
// ======================================

function showSuccess(text) {

    message.className =
        "message success";

    message.textContent =
        text;
}


function showError(text) {

    message.className =
        "message error";

    message.textContent =
        text;
}


function clearMessage() {

    message.className =
        "message";

    message.textContent =
        "";
}