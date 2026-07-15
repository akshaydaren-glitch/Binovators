document.addEventListener('DOMContentLoaded', function () {
  var collapseButtons = document.querySelectorAll('.collapse-button');

  collapseButtons.forEach(function (button) {
    button.addEventListener('click', function () {
      var targetId = button.getAttribute('data-target');
      var target = targetId ? document.getElementById(targetId) : button.nextElementSibling;

      if (!target) {
        return;
      }

      var isHidden = target.style.display === 'none';
      target.style.display = isHidden ? '' : 'none';
      button.setAttribute('aria-expanded', String(isHidden));
    });
  });
});
