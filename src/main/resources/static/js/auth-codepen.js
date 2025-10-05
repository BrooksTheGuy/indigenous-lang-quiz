(function () {
  const wrap = document.getElementById('main');
  if (!wrap) return;

  const signUpBtn = document.getElementById('signUpBtn');
  const loginBtn  = document.getElementById('loginBtn');

  function toSignup(e){ if(e) e.preventDefault(); wrap.classList.add('singUpActive'); wrap.classList.remove('loginActive'); }
  function toLogin(e){ if(e) e.preventDefault(); wrap.classList.add('loginActive');  wrap.classList.remove('singUpActive'); }

  if (signUpBtn) signUpBtn.addEventListener('click', toSignup);
  if (loginBtn)  loginBtn.addEventListener('click', toLogin);

  const params = new URLSearchParams(location.search);
  if (params.get('mode') === 'signup') toSignup();
})();
