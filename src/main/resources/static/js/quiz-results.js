// Sends a finished quiz attempt to /quiz/submit (Spring Security CSRF-safe)
(function () {
  function getCsrf() {
    const token = document.querySelector('meta[name="_csrf"]')?.content;
    const header = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';
    return { token, header };
  }

  /**
   * Call this when the quiz finishes
   * @param {{ language: string, total: number, correct: number, elapsedSeconds: number }} payload
   * @returns {Promise<object>} response JSON or {}
   */
  window.submitResults = async function (payload) {
    const { token, header } = getCsrf();

    const res = await fetch('/quiz/submit', {
      method: 'POST',
      credentials: 'same-origin', // include session cookie
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { [header]: token } : {})
      },
      body: JSON.stringify({
        language: payload.language,
        totalQuestions: payload.total,
        correct: payload.correct,
        elapsedSeconds: payload.elapsedSeconds
      })
    });

    try { return await res.json(); } catch { return {}; }
  };
})();
