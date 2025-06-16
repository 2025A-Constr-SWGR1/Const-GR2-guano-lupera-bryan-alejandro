package entidades;

public class Comparacion {
    private Restaurante restaurante1;
    private Restaurante restaurante2;
    private int puntuacionRest1;
    private int puntuacionRest2;
    private StringBuilder comparaciones;
    private String resultadoFinal;

    public Comparacion(Restaurante restaurante1, Restaurante restaurante2) {
        this.restaurante1 = restaurante1;
        this.restaurante2 = restaurante2;
        this.puntuacionRest1 = 0;
        this.puntuacionRest2 = 0;
        this.comparaciones = new StringBuilder();
        this.resultadoFinal = "";
    }

    public void realizarComparacion() {
        compararPuntajePromedio();
        compararTiempoEspera();
        compararDistancia();
        compararCalidad();
        compararPrecio();
        determinarGanador();
    }

    public String getComparaciones() {
        return comparaciones.toString();
    }

    public String getResultadoFinal() {
        return resultadoFinal;
    }

    private void compararPuntajePromedio() {
        if (restaurante1.getPuntajePromedio() != null && restaurante2.getPuntajePromedio() != null) {
            if (restaurante1.getPuntajePromedio() > restaurante2.getPuntajePromedio()) {
                puntuacionRest1++;
                agregarComparacion("• " + restaurante1.getNombre() + " tiene mejor puntaje (" + 
                    restaurante1.getPuntajePromedio() + " vs " + restaurante2.getPuntajePromedio() + ")<br>");
            } else if (restaurante2.getPuntajePromedio() > restaurante1.getPuntajePromedio()) {
                puntuacionRest2++;
                agregarComparacion("• " + restaurante2.getNombre() + " tiene mejor puntaje (" + 
                    restaurante2.getPuntajePromedio() + " vs " + restaurante1.getPuntajePromedio() + ")<br>");
            }
        }
    }

    private void compararTiempoEspera() {
        if (restaurante1.getTiempoEspera() < restaurante2.getTiempoEspera()) {
            puntuacionRest1++;
            agregarComparacion("• " + restaurante1.getNombre() + " tiene menor tiempo de espera (" + 
                restaurante1.getTiempoEspera() + " min vs " + restaurante2.getTiempoEspera() + " min)<br>");
        } else if (restaurante2.getTiempoEspera() < restaurante1.getTiempoEspera()) {
            puntuacionRest2++;
            agregarComparacion("• " + restaurante2.getNombre() + " tiene menor tiempo de espera (" + 
                restaurante2.getTiempoEspera() + " min vs " + restaurante1.getTiempoEspera() + " min)<br>");
        }
    }

    private void compararDistancia() {
        if (restaurante1.getDistanciaUniversidad() != null && restaurante2.getDistanciaUniversidad() != null) {
            if (restaurante1.getDistanciaUniversidad() < restaurante2.getDistanciaUniversidad()) {
                puntuacionRest1++;
                agregarComparacion("• " + restaurante1.getNombre() + " está más cerca (" + 
                    restaurante1.getDistanciaUniversidad() + " km vs " + restaurante2.getDistanciaUniversidad() + " km)<br>");
            } else if (restaurante2.getDistanciaUniversidad() < restaurante1.getDistanciaUniversidad()) {
                puntuacionRest2++;
                agregarComparacion("• " + restaurante2.getNombre() + " está más cerca (" + 
                    restaurante2.getDistanciaUniversidad() + " km vs " + restaurante1.getDistanciaUniversidad() + " km)<br>");
            }
        }
    }

    private void compararCalidad() {
        if (restaurante1.getCalidad() > restaurante2.getCalidad()) {
            puntuacionRest1++;
            agregarComparacion("• " + restaurante1.getNombre() + " tiene mejor calidad (" + 
                restaurante1.getCalidad() + " vs " + restaurante2.getCalidad() + ")<br>");
        } else if (restaurante2.getCalidad() > restaurante1.getCalidad()) {
            puntuacionRest2++;
            agregarComparacion("• " + restaurante2.getNombre() + " tiene mejor calidad (" + 
                restaurante2.getCalidad() + " vs " + restaurante1.getCalidad() + ")<br>");
        }
    }

    private void compararPrecio() {
        if (restaurante1.getPrecio() < restaurante2.getPrecio()) {
            puntuacionRest1++;
            agregarComparacion("• " + restaurante1.getNombre() + " tiene mejor precio (" + 
                restaurante1.getPrecio() + " vs " + restaurante2.getPrecio() + ")<br>");
        } else if (restaurante2.getPrecio() < restaurante1.getPrecio()) {
            puntuacionRest2++;
            agregarComparacion("• " + restaurante2.getNombre() + " tiene mejor precio (" + 
                restaurante2.getPrecio() + " vs " + restaurante1.getPrecio() + ")<br>");
        }
    }

    private void agregarComparacion(String comparacion) {
        comparaciones.append(comparacion);
    }

    private void determinarGanador() {
        if (puntuacionRest1 > puntuacionRest2) {
            resultadoFinal = "<strong>" + restaurante1.getNombre() + "</strong> es la mejor opción con " + 
                puntuacionRest1 + " ventajas contra " + puntuacionRest2;
        } else if (puntuacionRest2 > puntuacionRest1) {
            resultadoFinal = "<strong>" + restaurante2.getNombre() + "</strong> es la mejor opción con " + 
                puntuacionRest2 + " ventajas contra " + puntuacionRest1;
        } else {
            resultadoFinal = "Ambos restaurantes están empatados con " + 
                puntuacionRest1 + " ventajas cada uno";
        }
    }
}