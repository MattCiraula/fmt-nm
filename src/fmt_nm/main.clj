(ns fmt-nm.main
  (:require [clojure.java.shell :as shell]
            [clojure.string :as string]))

;; TODO: get native comp working

(defn format-file-name
  "We will start by changing TitleCase to kebab-case"
  [name]
  (:res
   (reduce (fn [{:keys [res past] :as acc} letter]
             ;; may want to refactor, could put a cond to check if (last res) == - and drop
             (cond (java.lang.Character/isUpperCase letter)
                   (if (or (= res "")
                           (java.lang.Character/isUpperCase (last past))
                           (= (last res) \-))
                     (-> acc
                         (update :res str (string/lower-case letter))
                         (update :past str letter))
                     (-> acc
                         (update :res str "-" (string/lower-case letter))
                         (update :past str letter)))
                   (contains? #{\space \_ \- \, \.} letter)
                   (if (= \- (last res))
                     (update acc :past str letter) ; don't put 2 dashes in a row
                     (-> acc
                         (update :res str "-")
                         (update :past str letter)))
                   :else
                   (-> acc
                       (update :res str letter)
                       (update :past str letter))))
           {:res "" :past ""}
           name)))

(defn separate-file-name
  "Calls re-find on the regex for separating file name from path. Made a fun for testing"
  [path]
  (re-find #"^(.*\/)?(?:$|(.+?)(?:(\.[^.]*$)|$))" path))

(defn -main
  [dir & _args]
  (let [res (shell/sh "find" dir "-type" "f")
        err (:err res)
        files (string/split (:out res) #"\n")
        separated (map separate-file-name files)]
    (if (= err "")
      (dorun (map (fn [[og path file ext]]
                      (let [formatted (str path (format-file-name file) ext)]
                        (when-not (= formatted og)
                          ;; TODO: research mapping with side effects
                          ;; running a shell command from map might be a bad idea
                          (shell/sh "mv" og formatted))))
                    separated))
      (println "Error: " err)))
  (System/exit 0))
