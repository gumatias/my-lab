(ns github-issues.formatter)

(defn- format-attachment
  [issue]
  {
    :title (:title issue)
    :title_link (:html_url issue)
    :text (format "Review App: https://vts-dev-pr-%s.herokuapp.com" (:number issue))
  })

(defn format-issues
  [issues]
  {
    :text "Here are the issues found"
    :attachments (map format-attachment issues)
  })

