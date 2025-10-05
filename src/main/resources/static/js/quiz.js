// v15 – robust + summary/advice card

const banks = {
  "Oshiwambo":[
    {text:"How do you say ‘Hello’ in Oshiwambo?",choices:["Wa uhlala","Wa uhala po","Onawa","Moro"],answer:"Wa uhala po"},
    {text:"Translate ‘I love you’ to Oshiwambo.",choices:["Onda ku hole","Nakulata","Te amo","Ndiku hole"],answer:"Onda ku hole"},
    {text:"‘Thank you’ in Oshiwambo is…",choices:["Tangi unene","Ni shangwe","Okuhepa","Gracias"],answer:"Tangi unene"},
    {text:"Pick the Oshiwambo word for ‘Water’.",choices:["Omeva","Mazi","Mvula","Agua"],answer:"Omeva"},
    {text:"Common greeting/response pair:",choices:["Wa uhala po — Onawa","Moro — Onawa","U shani — Ndalumba"],answer:"Wa uhala po — Onawa"}
  ],
  "Herero":[
    {text:"How do you say ‘Hello’ in Herero?",choices:["Wa uhala","Moro","Jambo","Halo"],answer:"Moro"},
    {text:"Translate ‘I love you’ to Herero.",choices:["Ndi ku nawa","Onda ku hole","Te quiero","Nakulata"],answer:"Ndi ku nawa"},
    {text:"‘Thank you’ in Herero is…",choices:["Okuhepa","Gracias","Tangi unene","Ni shangwe"],answer:"Okuhepa"},
    {text:"‘Cow’ in Herero is…",choices:["Ongombe","Engombe","Inkine","Ngombe"],answer:"Ongombe"},
    {text:"Pick the Herero word for ‘Water’.",choices:["Omeva","Omezire","Okuruo","Ehi"],answer:"Omeva"},
    {text:"Common greeting/response pair:",choices:["Moro — Ndje po","Omeva — Okuhepa","Ehi — Okuruo"],answer:"Moro — Ndje po"}
  ],
  "Silozi":[
    {text:"How do you say ‘Hello’ in Silozi?",choices:["Muli shani","U shani","U shani bo","Ni hao"],answer:"U shani"},
    {text:"Translate ‘I love you’ to Silozi.",choices:["Nakulipa","Nakulata","Nakulata bo","Nakulata po"],answer:"Nakulata"},
    {text:"‘Thank you’ in Silozi is…",choices:["Ni shangwe","Ni shangwe sana","Ni shangwe kakulu","Asante"],answer:"Ni shangwe"},
    {text:"Pick the Silozi word for ‘Water’.",choices:["Mazi","Amei","Metsi","Mvula"],answer:"Mazi"},
    {text:"Common greeting/response pair:",choices:["U shani — Ndalumba","U shani — Mwa hata","U shani — Ni shangwe kakulu"],answer:"U shani — Ndalumba"}
  ],
  "Rukwangali":[
    {text:"How do you say ‘Hello’ in Rukwangali?",choices:["Wa uhala","Moro","Nda kwere","Nawa"],answer:"Nda kwere"},
    {text:"Translate ‘Thank you’ to Rukwangali.",choices:["Tangi mono","Okuhepa","Ni shangwe","Tangi unene"],answer:"Tangi mono"},
    {text:"‘Love’ in Rukwangali is…",choices:["Rudo","Love","Nguva","Ondjendje"],answer:"Ondjendje"},
    {text:"Pick the Rukwangali word for ‘Water’.",choices:["Omeva","Omayi","Amai","Meme"],answer:"Omayi"},
    {text:"Common greeting/response pair:",choices:["Nda kwere — Nawa","Moro — Onawa","U shani — Ndalumba"],answer:"Nda kwere — Nawa"}
  ]
};

// helpers
const byId = id => document.getElementById(id);
const set = (el, fn) => { if (el) fn(el); };

// cache
const els = {
  gate: byId('langGate'),
  quiz: byId('quiz'),
  question: byId('questionText'),
  choices: byId('choices'),
  next: byId('nextBtn'),
  changeLang: byId('changeLangBtn'),
  progress: byId('progress'),
  progressBar: byId('progressBar'),
  feedback: byId('feedback'),
  results: byId('results'),
  scoreText: byId('scoreText'),
  scorePct: byId('scorePct'),
  grade: byId('grade'),
  gradePill: byId('gradePill'),
  elapsed: byId('elapsed'),
  elapsedDup: byId('elapsedDup'),
  retry: byId('retryBtn'),
  langBadge: byId('langBadge'),
  hsLang: byId('hsLang'),
  hsBody: byId('highscoresBody'),
  review: byId('review'),
  scoreLang: byId('scoreLang'),
  summaryCard: byId('summaryCard'),
  summaryMsg: byId('summaryMsg')
};

let currentBank = [];
let currentLang = "";
let currentIndex = 0;
let score = 0;
let startedAt = null;
let timerId = null;
let review = [];

// language selection
document.querySelectorAll('.lang-btn').forEach(btn => {
  btn.addEventListener('click', () => startQuiz(btn.dataset.lang));
});

function startQuiz(lang) {
  currentLang = lang || "";
  currentBank = [...(banks[currentLang] || [])];
  currentIndex = 0;
  score = 0;
  review = [];
  startedAt = Date.now();
  if (timerId) clearInterval(timerId);
  timerId = setInterval(updateElapsed, 200);

  els.gate.classList.add('hidden');
  els.quiz.classList.remove('hidden');
  els.results.classList.add('hidden');

  set(els.langBadge, el => el.textContent = '— ' + currentLang);
  renderQuestion();
  updateProgress();
}

function updateElapsed() {
  const ms = Date.now() - startedAt;
  const s = Math.floor(ms / 1000);
  const m = Math.floor(s / 60);
  const remS = String(s % 60).padStart(2, '0');
  set(els.elapsed, el => el.textContent = `${m}:${remS}`);
  set(els.elapsedDup, el => el.textContent = `${m}:${remS}`);
}

function renderQuestion() {
  const q = currentBank[currentIndex];
  set(els.question, el => el.textContent = q.text);
  els.choices.innerHTML = '';
  q.choices.forEach(choice => {
    const li = document.createElement('li');
    li.className = 'choice';
    li.innerHTML = `<span>${choice}</span><span class="hint">Select Answer</span>`;
    li.addEventListener('click', () => selectChoice(li, choice, q.answer));
    els.choices.appendChild(li);
  });
  els.feedback.classList.add('hidden');
  els.next.disabled = true;
}

function selectChoice(li, chosen, answer) {
  const q = currentBank[currentIndex];
  const correct = chosen === answer;

  document.querySelectorAll('.choice').forEach(c => c.classList.remove('correct','incorrect'));
  li.classList.add(correct ? 'correct' : 'incorrect');

  set(els.feedback, el => {
    el.textContent = correct ? 'Nice!' : `Oops! Correct answer is ${answer}.`;
    el.className = 'alert ' + (correct ? 'success' : 'error');
    el.classList.remove('hidden');
  });

  if (correct) score++;
  review.push({ q: q.text, chosen, answer, correct });

  els.next.disabled = false;
}

els.next.addEventListener('click', () => {
  currentIndex++;
  if (currentIndex >= currentBank.length) finish();
  else { renderQuestion(); updateProgress(); }
});

els.changeLang.addEventListener('click', resetToGate);
set(els.retry, el => el.addEventListener('click', resetToGate));

function updateProgress() {
  set(els.progress, el => el.textContent = `Question ${currentIndex + 1} of ${currentBank.length}`);
  const pct = Math.round((currentIndex / currentBank.length) * 100);
  set(els.progressBar, el => el.style.width = `${pct}%`);
}

function finish() {
  if (timerId) { clearInterval(timerId); timerId = null; }
  const pct = Math.round((score / currentBank.length) * 100);

document.querySelector('.score-circle')?.style.setProperty('--pct', pct + '%');

  els.quiz.classList.add('hidden');
  els.results.classList.remove('hidden');

  // headline section
  set(els.scoreLang, el => el.textContent = currentLang ? `— ${currentLang}` : '—');

  // score/grade
  set(els.scoreText, el => el.textContent = `${score} / ${currentBank.length}`);
  set(els.scorePct, el => el.textContent = `${pct}%`);
  const gradeLetter = pct >= 80 ? 'A' : pct >= 60 ? 'B' : pct >= 40 ? 'C' : 'F';
  set(els.grade, el => el.textContent = gradeLetter);
  set(els.gradePill, el => { el.textContent = gradeLetter; el.classList.toggle('good', pct >= 60); });

  // summary message like your old UI
  const msg =
    pct >= 80 ? "Excellent! You're nailing it—try a harder set next."
  : pct >= 60 ? "Good job—review a few tricky items, then retake the quiz."
  : pct >= 40 ? "Nice attempt—start with greetings & core vocab, then try again."
              : "No stress—start with greetings & core vocab, then retake the quiz.";
  set(els.summaryMsg, el => el.textContent = msg);
  set(els.summaryCard, el => el.classList.remove('hidden'));

  // highscores (per-language)
  try {
    const entry = { score, pct, time: (els.elapsed?.textContent || '0:00'), date: new Date().toLocaleString() };
    const key = `hs_${currentLang || 'unknown'}`;
    const list = JSON.parse(localStorage.getItem(key) || '[]');
    list.unshift(entry);
    localStorage.setItem(key, JSON.stringify(list.slice(0, 10)));
    renderHighscores(list);
  } catch (e) {
    console.warn('Highscores unavailable:', e);
    renderHighscores([]);
  }

  renderReview();
  set(els.progressBar, el => el.style.width = '100%');
}

function renderHighscores(list) {
  if (!els.hsBody) return;
  els.hsBody.innerHTML = (list || []).map((e, i) => `
    <tr>
      <td>${i+1}</td>
      <td>${e.score}</td>
      <td>${e.pct}%</td>
      <td>${e.time}</td>
      <td>${e.date}</td>
    </tr>
  `).join('');
}

function renderReview() {
  if (!els.review) return;
  els.review.innerHTML = review.map((r, idx) => `
    <div class="${r.correct ? 'ok' : 'bad'}">
      <p><strong>${idx+1}. ${r.q}</strong><br>
      Your answer: ${r.chosen} ${r.correct ? '✅' : '❌'} |
      Correct: <strong>${r.answer}</strong></p>
    </div>
  `).join('');
}

function resetToGate() {
  els.gate.classList.remove('hidden');
  els.quiz.classList.add('hidden');
  els.results.classList.add('hidden');
  set(els.progressBar, el => el.style.width = '0%');
  set(els.elapsed, el => el.textContent = '0:00');
  set(els.elapsedDup, el => el.textContent = '0:00');
}
