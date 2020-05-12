var domReady = function(callback) {
    document.readyState === "interactive" || document.readyState === "complete" ? callback() : document.addEventListener("DOMContentLoaded", callback);
};

window.onload = function () {
    let mdl_checkboxes = document.getElementsByClassName('mdl-checkbox');
    console.log(mdl_checkboxes);
    for(let mdl_checkbox of mdl_checkboxes) {
        mdl_checkbox.addEventListener('click', function(e) {
            let input = this.querySelector('input.mdl-checkbox__input');
            input.checked = !input.checked;
            if(input.checked) {
                input.setAttribute('name', 'ids[]');
                input.setAttribute('value', input.closest('tr').getAttribute('data-id'));
            } else {
                input.setAttribute('name', '');
                input.setAttribute('value', '');
            }
        });
    }

    let checkboxes = document.getElementsByClassName('mdl-checkbox__input');
    for(let checkbox of checkboxes) {
        checkbox.addEventListener('change', function (e) {
            let checked_elements = 0;
            let cbs = document.getElementsByClassName('mdl-checkbox__input');
            for (let cb of cbs) {
                if (cb.checked)
                    checked_elements++;
            }
            if (checked_elements)
                document.getElementsByClassName('delete-book-button')[0].classList.remove("delete-book-button--hidden");
            else
                document.getElementsByClassName('delete-book-button')[0].classList.add("delete-book-button--hidden");
        });
    }
}